//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated from a template.
//
//     Manual changes to this file may cause unexpected behavior in your application.
//     Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace Capstone.Models.Entities
{
    using System;
    using System.Collections.Generic;
    
    public partial class Area
    {
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2214:DoNotCallOverridableMethodsInConstructors")]
        public Area()
        {
            this.ParkingLots = new HashSet<ParkingLot>();
        }
    
        public int Id { get; set; }
        public string Name { get; set; }
        public Nullable<int> Address { get; set; }
        public int CarParkId { get; set; }
        public int Status { get; set; }
        public bool Active { get; set; }
    
        public virtual CarPark CarPark { get; set; }
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<ParkingLot> ParkingLots { get; set; }
    }
}
